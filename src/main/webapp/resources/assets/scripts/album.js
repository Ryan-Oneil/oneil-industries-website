function checkAlbum() {
    console.log(document.getElementById('albumoption').value);
    if(document.getElementById('albumoption').value == "new") {
        document.getElementById('newAlbumOptions').style.display = "block";
    }else {
        document.getElementById('newAlbumOptions').style.display = "none";
    }
}