window.saveApply = function($scope) {
	if ($scope.$root.$$phase !== '$apply' && $scope.$root.$$phase !== '$digest') {
		console.log("update!");
	    $scope.$apply();
	} else {
		console.log("Snap!");
	}
}

window.getToken = function(cookie_name) {
	var read_cookies = document.cookie;
	var split_read_cookie = read_cookies.split(";");
	for (i=0;i<split_read_cookie.length;i++){
		var value=split_read_cookie[i];
		value=value.split("=");
		var str = value[0].replace(/\s+/g, '');
		if(str===cookie_name){
			console.log("for "+cookie_name+" token found", value[1], split_read_cookie.length);
			return value[1];
		}
	}
	console.warn("for "+cookie_name+" NO token found");
	return null;
}

window.sendRequest = function(url, fn, additionalHeaders) {
	var request = new XMLHttpRequest();
	request.onreadystatechange = fn;
	request.open('POST', url, true);
	request.setRequestHeader("token", getToken("token"));
	request.setRequestHeader("username", getToken("username"));
	if(additionalHeaders) {
		for(var key in additionalHeaders) {
			if(additionalHeaders.hasOwnProperty(key)) {
				request.setRequestHeader(key, additionalHeaders[key]);
			}
		}
	}
	request.send();
}