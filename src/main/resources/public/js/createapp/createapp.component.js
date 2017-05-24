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
