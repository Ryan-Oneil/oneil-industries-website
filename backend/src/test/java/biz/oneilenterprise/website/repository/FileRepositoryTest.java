package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.SharedFile;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FileRepositoryTest extends BaseRepository {

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void getByIDTest() {
        Optional<SharedFile> foundFile = fileRepository.getById("lBOmv010VZLaK61o");

        assertThat(foundFile.isPresent()).isTrue();
        foundFile.ifPresent(sharedFile -> assertThat(sharedFile.getId()).isEqualTo("lBOmv010VZLaK61o"));
        assertThat(foundFile.get().getLink().getCreator().getUsername().equals("test"));
    }

    @Test
    public void getTotalFileCountTest() {
        long count = fileRepository.getTotalFiles();

        assertThat(count).isEqualTo(17);
    }

    @Test
    public void getTotalFileCountByUser() {
        long count = fileRepository.getUserFileCount("test");

        assertThat(count).isEqualTo(17);
    }
}
