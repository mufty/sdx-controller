function pageSetup() {
    clientSideInclude('navbar', 'menu.html');
    clientSideInclude('content', gup('art') + '.html');
}

// credit to: http://www.boutell.com/newfaq/creating/include.html
function clientSideInclude(id, url) {
    var req = false;
    if (window.XMLHttpRequest) {
        // For Safari, Firefox, and other non-MS browsers
        try {
            req = new XMLHttpRequest();
        } catch (e) {
            req = false;
        }
    } else if (window.ActiveXObject) {
        // For Internet Explorer on Windows
        try {
            req = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
                req = false;
            }
        }
    }

    var element = document.getElementById(id);
    if (!element) {
        alert("Bad id " + id + 
        "passed to clientSideInclude." +
        "You need a div or span element " +
        "with this id in your page.");
        return;
    }
    
    if (req) {
        // Synchronous request, wait till we have it all
        req.open('GET', url, false);
        req.send(null);
        element.innerHTML = req.responseText;
    } else {
        element.innerHTML =
        "Sorry, your browser does not support " +
        "XMLHTTPRequest objects. This page requires " +
        "Internet Explorer 5 or better for Windows, " +
        "or Firefox for any system, or Safari. Other " +
        "compatible browsers may also exist.";
    }
}

// credit to: http://www.netlobo.com/url_query_string_javascript.html
function gup(name){
    name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");  
    var regexS = "[\\?&]"+name+"=([^&#]*)";  
    var regex = new RegExp( regexS );  
    var results = regex.exec( window.location.href );
    
    if( results == null )    
        return 'whatis';  
    else    
        return results[1];
}