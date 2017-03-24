angular.
	module('lauchpad').
	component('lauchpad', {
		templateUrl: '/js/lauchpad/lauchpad.template.html',
		controller: ["$scope",
			function LauchpadController($scope) {
				var self = this,
				scope = $scope,
				request = new XMLHttpRequest();
				
				self.msg = "";
				self.button_label = "Back to login";
				
				request.onreadystatechange = getResponse;
				request.open('POST', '/auth', true);
				request.setRequestHeader("TOKEN", getToken());
				request.setRequestHeader("username", localStorage.getItem("username"));
				request.send();
				
				
				function getResponse() {
					if (request.readyState === XMLHttpRequest.DONE) {
						if (request.status === 200) {
							console.log("response text: " + request.responseText, self);
							self.msg = "COOL " + request.responseText;
							self.button_label = "Logout";
							scope.$apply();
						} else {
							console.log('There was a problem with the request.');
							self.msg = "";
							self.button_label = "Back to login";
							scope.$apply();
						}
					}
				}
				
				function getToken() {
					var read_cookies = document.cookie;
					var split_read_cookie = read_cookies.split(";");
					for (i=0;i<split_read_cookie.length;i++){
						var value=split_read_cookie[i];
						value=value.split("=");
						if(value[0]=="token"){
							return value[1];
						}
					}
				}
				
			}
		]
	}
);
