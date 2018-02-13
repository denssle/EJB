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
				
				request.onreadystatechange = createAppList;
				request.open('POST', '/auth', true);
				request.setRequestHeader("token", getToken("token"));
				request.setRequestHeader("username", getToken("username"));
				request.send();
				
				function createAppList() {
					if (request.readyState === XMLHttpRequest.DONE) {
						if (request.status === 200) {
							self.msg = "Hello " + getToken("username") + ".";
							self.button_label = "Logout";
							self.response = JSON.parse(request.responseText);
							console.log("response text: ", self.response);
							scope.$apply();
						} else {
							console.log('There was a problem with the request.');
							self.msg = "";
							self.button_label = "Back to login";
						}
					}
				}
				
				function getApp(id) {
					for(var i=0; i<self.response.length; i++) {
						var app = self.response[i];
						if(app.id === id) {
							return app;
						}
					}
					return null;
				}
 				
				$scope.openApp = function(id) {
					var app = getApp(id);
					console.log("openApp", id, app);
					if (app !== null) {
						window.location = app.url;
					} else {
						console.warn("no app found");
					}
				}
				
				
			}
		]
	}
);

