angular.
	module('createapp').
	component('createapp', {
		templateUrl: '/js/createapp/createapp.template.html',
		controller: ["$scope",
			function CreateAppController($scope) {
				var request = new XMLHttpRequest(),
				self = this,
				scope = $scope;
				scope.response = {};
				request.onreadystatechange = getTemplats;
				request.open('POST', '/getTemplates', true);
				request.setRequestHeader("token", getToken("token"));
				request.setRequestHeader("username", getToken("username"));
				request.send();
				
				function getTemplats() {
					if (request.readyState === XMLHttpRequest.DONE) {
						if (request.status === 200) {
							self.response = JSON.parse(request.responseText);
							console.log("response text: ", self.response);
							scope.$apply();
						} else {
							console.log('There was a problem with the request.');
						}
					}
				}
				
				function getToken(cookie_name) {
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
				}
				
				$scope.createApp = function() {
					var request = new XMLHttpRequest();
					request.open('POST', "/createapp", true); //Synchronous XMLHttpRequest on the main thread is deprecated
					request.setRequestHeader("token", getToken("token"));
					request.setRequestHeader("username", getToken("username"));
					request.setRequestHeader("app_name", scope.app_name);
					request.setRequestHeader("app_url", scope.app_url);
					request.setRequestHeader("app_description", scope.app_description);
					request.setRequestHeader("app_template_id", scope.app_template.id);
					console.log(request);
					request.onreadystatechange = function(){
						if (request.readyState === XMLHttpRequest.DONE) {
							if (request.status === 200) {
								scope.goBack();
							}
						}
					};
					request.send();
				}
				
				$scope.goBack = function() {
					window.location = "/#!/lauchpad";
				}
		}]
	});
