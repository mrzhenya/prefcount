// changes current locale - assumes that there is one-to-one
// mapping for English and Russian pages (IMPORTANT!);
// note that this doesn't work on Windows file system right now
function changeLocale(isToRussian) {
  var loc = window.location;
  var newPath = "";
  if (loc.href.indexOf('/ru/') > 0) {
    // we have Russian locale set
    if (isToRussian) {
      return;
    }
    newPath = loc.href.replace('/ru/', '/');

  } else {
    // we have default (English) locale set
    if (!isToRussian) {
      return;
    }
    if (loc.protocol.indexOf('http') == 0) {
      // we are running on an http server
      // note, that the siteHref value is set in the site.vm
      newPath = loc.href.replace(siteHref, siteHref + "/ru");
    } else {
      // assuming we are on a file system and in the target directory
      newPath = loc.href.replace("/target/site/", "/target/site/ru/");
    }
  }
  loc.href = newPath;
}

// this function replaces a thumb image with its
// large counterpart (and also does the opposite)
function toggleImageSize(aRef) {
  var imgRef = aRef.firstChild;
  var currSrc = imgRef.src;
  var newSrc;
  if (currSrc.indexOf("-t.jpg") > 0) {
    newSrc = currSrc.replace("-t.jpg", ".jpg");
  } else {
    newSrc = currSrc.replace(".jpg", "-t.jpg");
  }
  imgRef.src = newSrc;
}
