package biz.oneilenterprise.website.service;

import biz.oneilenterprise.website.dto.FileDTO;
import biz.oneilenterprise.website.dto.LinkDTO;
import biz.oneilenterprise.website.entity.Link;
import biz.oneilenterprise.website.entity.LinkView;
import biz.oneilenterprise.website.entity.SharedFile;
import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.exception.LinkException;
import biz.oneilenterprise.website.exception.ResourceNotFoundException;
import biz.oneilenterprise.website.utils.FileHandlerUtil;
import biz.oneilenterprise.website.repository.FileRepository;
import biz.oneilenterprise.website.repository.LinkRepository;
import biz.oneilenterprise.website.repository.LinkViewRepository;
import biz.oneilenterprise.website.utils.RandomIDGenUtil;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ShareLinkService {

    private final LinkRepository linkRepository;
    private final FileRepository fileRepository;
    private final LinkViewRepository viewRepository;

    private static final Logger logger = LogManager.getLogger(ShareLinkService.class);
    private static final int UUID_LENGTH = 16;

    @Value("${service.fileSharing.location}")
    private String sharedFilesDirectory;

    public ShareLinkService(LinkRepository linkRepository, FileRepository fileRepository, LinkViewRepository viewRepository) {
        this.linkRepository = linkRepository;
        this.fileRepository = fileRepository;
        this.viewRepository = viewRepository;
    }

    public LinkDTO generateShareLink(User user, String expires, String title, List<File> files) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        long sizeOfFiles = files.stream().mapToLong(File::length).sum();
        Link link = new Link(generateLinkUUID(UUID_LENGTH), title, user, format.parse(expires), new Date(), sizeOfFiles);

        link.setFiles(createSharedFiles(files, link));
        linkRepository.save(link);

        //Checks to see if the files parent directory matches the link id
        //If files are uploaded using rest API then it is first put into a temp folder
        if (!renameLinkDirectory(files.get(0), link.getId(), user.getUsername())) {
            logger.error("Unable to rename directory " + files.get(0).getParent());
            throw new LinkException("Error changing directory name");
        }
        return linkToDTO(link);
    }

    public boolean renameLinkDirectory(File file, String newLinkID, String creator) {
        File parent = file.getParentFile();

        if (!parent.getName().equals(newLinkID)) {
            File reNamedDirectory = new File(getLinkDirectory(creator, newLinkID));

            return parent.renameTo(reNamedDirectory);
        }
        return true;
    }

    public List<SharedFile> createSharedFiles(List<File> files, Link link) {
        return files.stream().map(file -> new SharedFile(generateFileUUID(UUID_LENGTH), file.getName(), file.length(), link))
            .collect(Collectors.toList());
    }

    public String generateLinkUUID(int length) {
        String id = RandomIDGenUtil.getBase62(length);
        Optional<Link> link = getLink(id);

        while (link.isPresent()) {
            id = RandomIDGenUtil.getBase62(length);

            link = getLink(id);
        }
        return id;
    }

    public String generateFileUUID(int length) {
        String id = RandomIDGenUtil.getBase62(length);
        Optional<SharedFile> file = getFile(id);

        while (file.isPresent()) {
            id = RandomIDGenUtil.getBase62(length);

            file = getFile(id);
        }
        return id;
    }

    public Optional<Link> getLink(String id) {
        return linkRepository.findById(id);
    }

    public Link getLinkCheckPresence(String linkID) {
        return getLink(linkID).orElseThrow(() -> new ResourceNotFoundException("This shared link doesn't exist"));
    }

    public Link getLinkValidate(String linkID) {
        Link link = getLinkCheckPresence(linkID);

        checkLinkExpiry(link);

        return link;
    }

    public Link getLinkFileWithValidation(String linkID) {
        return linkRepository.getById(linkID).orElseThrow(() -> new ResourceNotFoundException("This shared link doesn't exist"));
    }

    public void checkLinkExpiry(Link link) {
        if (isExpired(link.getExpiryDatetime())) throw new LinkException("This link has expired");
    }

    public boolean isExpired(Date date) {
        Calendar cal = Calendar.getInstance();
        return (date.getTime() - cal.getTime().getTime()) <= 0;
    }

    public Link deleteLink(String linkID) {
        Link link = getLinkFileWithValidation(linkID);
        User user = link.getCreator();

        FileHandlerUtil.deleteDirectory(getLinkDirectory(user.getUsername(), linkID));
        linkRepository.delete(link);

        return link;
    }

    public SharedFile deleteFile(String fileID) {
        SharedFile file = checkFileLinkValidation(fileID);
        Link link = file.getLink();

        link.setSize(link.getSize() - file.getSize());
        fileRepository.delete(file);
        linkRepository.save(link);

        String fileLocation = getFileLocation(link.getCreator().getUsername(), link.getId(), file.getName());
        FileHandlerUtil.deleteFile(fileLocation);

        return file;
    }

    public Optional<SharedFile> getFile(String id) {
        return fileRepository.findById(id);
    }

    public Optional<SharedFile> getFileWithLink(String id) {
        return fileRepository.getById(id);
    }

    public SharedFile checkFileLinkValidation(String name) {
        Optional<SharedFile> file = getFileWithLink(name);

        if (!file.isPresent()) throw new ResourceNotFoundException("Invalid File");
        checkLinkExpiry(file.get().getLink());

        return file.get();
    }

    public String getFileLocation(String user, String linkID, String fileName) {
        return String.format(sharedFilesDirectory + "%s/%s", user, linkID, fileName);
    }

    public String getLinkDirectory(String user, String linkID) {
        return String.format(sharedFilesDirectory + "%s", user, linkID);
    }

    public List<FileDTO> addFilesToLink(String linkID, List<File> files) {
        Link link = getLinkValidate(linkID);

        long sizeOfFiles = files.stream().mapToLong(File::length).sum();
        List<SharedFile> sharedFiles = createSharedFiles(files, link);
        link.setSize(link.getSize() + sizeOfFiles);

        linkRepository.save(link);
        fileRepository.saveAll(sharedFiles);

        FileHandlerUtil.moveNewFilesToDirectory(files, getLinkDirectory(link.getCreator().getUsername(), link.getId()));

        return filesToDTO(sharedFiles);
    }

    public void editLink(String linkID, String title, String expires) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date date = format.parse(expires);
        Link link = getLinkValidate(linkID);

        link.setTitle(title);
        link.setExpiryDatetime(date);
        linkRepository.save(link);
    }

    public HashMap<String, Object> getUserStats(String user) {
        HashMap<String, Object> stats = new HashMap<>();

        stats.put("totalLinks", linkRepository.getUserLinkCount(user));
        stats.put("totalFiles", fileRepository.getUserFileCount(user));
        stats.put("totalViews", linkRepository.getUserTotalViews(user));
        stats.put("recentLinks", linksToDTO(linkRepository.getTop5ByCreator_UsernameOrderByCreationDateDesc(user)));
        stats.put("mostViewedLinks", linksToDTO(linkRepository.getTop5ByCreator_UsernameOrderByViewsDesc(user)));

        return stats;
    }

    @Async
    public void registerLinkView(Link link, String viewerIP) {
        Optional<LinkView> wasViewed = viewRepository.getFirstByIpAndLink(viewerIP, link);

        if (!wasViewed.isPresent()) {
            LinkView view = new LinkView(viewerIP, link);
            viewRepository.save(view);

            link.setViews(link.getViews() + 1);
            linkRepository.save(link);
        }
    }

    public HashMap<String, Object> getAdminLinkStats() {
        HashMap<String, Object> stats = new HashMap<>();

        stats.put("totalLinks", linkRepository.getTotalLinks());
        stats.put("totalViews", linkRepository.getTotalViews());
        stats.put("totalFiles", fileRepository.getTotalFiles());
        stats.put("recentShared", linksToDTO(linkRepository.findTop5ByOrderByIdDesc()));
        stats.put("mostViewed", linksToDTO(linkRepository.findTop5ByOrderByViewsDesc()));

        return stats;
    }

    public HashMap<String, Object> getLinksPageable(Pageable pageable) {
        HashMap<String, Object> links = new HashMap<>();
        links.put("links", linksToDTO(linkRepository.findAll(pageable).toList()));
        links.put("total", linkRepository.count());

        return links;
    }

    public Integer getUserTotalLinkCount(String username) {
        return linkRepository.getUserLinkCount(username);
    }

    public HashMap<String, Object> getUserLinks(String username, Pageable pageable) {
        HashMap<String, Object> pageableLinks = new HashMap<>();
        pageableLinks.put("total", linkRepository.getUserLinkCount(username));
        pageableLinks.put("links", linksToDTO(linkRepository.getAllByCreator_Username(username, pageable)));

        return pageableLinks;
    }

    public List<LinkDTO> linksToDTO(List<Link> links) {
        return links.stream()
            .map(this::linkToDTO)
            .collect(Collectors.toList());
    }

    public LinkDTO linkToDTO(Link link) {
        return new LinkDTO(link.getTitle(), link.getId(), link.getExpiryDatetime(), link.getSize(), link.getViews(), link.getCreationDate(),
            link.getCreator().getUsername());
    }

    public LinkDTO linkToDTO(Link link, List<FileDTO> files) {
        return new LinkDTO(link.getTitle(), link.getId(), link.getExpiryDatetime(), files, link.getSize(), link.getViews(), link.getCreationDate(),
            link.getCreator().getUsername());
    }

    public FileDTO fileToDTO(SharedFile file) {
        return new FileDTO(file.getId(), file.getName(), file.getSize());
    }

    public List<FileDTO> filesToDTO(List<SharedFile> files) {
        return files.stream()
            .map(this::fileToDTO)
            .collect(Collectors.toList());
    }
}
