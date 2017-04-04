angular.
	module('manager').
	component('manager', {
		templateUrl: '/js/manager/manager.template.html',
		controller: ["$scope",
			function managerController($scope) {
			var self = this,
			backup_user;
			self.user=[];
			self.tenplates = [];
			function sendRequest(url, fn, map) {
				var request = new XMLHttpRequest();
				request.onreadystatechange = fn;
				request.open('POST', url, true);
				request.setRequestHeader("token", getToken("token"));
				request.setRequestHeader("username", getToken("username"));
				if(map) {
					for(var key in map) {
						if(map.hasOwnProperty(key)) {
							request.setRequestHeader(key, map[key]);
						}
					}
				}
				request.send();
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
			function getUsers(response) {
				var request = response.srcElement;
				if (request.readyState === XMLHttpRequest.DONE) {
					if (request.status === 200) {
						self.users = JSON.parse(request.responseText);
						console.log("response text for users: ", self.users);
						if ($scope.$root.$$phase !== '$apply' && $scope.$root.$$phase !== '$digest') {
							console.log("user update!");
						    $scope.$apply();
						} else {
							console.log("Snap!");
						}
					} else {
						console.log('There was a problem with the request.', request.status);
					}
				}
			}
			function getTemplates(response) {
				var request = response.srcElement;
				if (request.readyState === XMLHttpRequest.DONE) {
					if (request.status === 200) {
						self.templates = JSON.parse(request.responseText);
						console.log("response text for templates: ", self.templates);
						if ($scope.$root.$$phase !== '$apply' && $scope.$root.$$phase !== '$digest') {
							console.log("template update!");
						    $scope.$apply();
						} else {
							console.log("Snap!");
						}
					} else {
						console.log('There was a problem with the request.', request.status);
					}
				}
			}

			function getUser(id) {
				for(var i=0; i<self.users.length; i++) {
					var user = self.users[i];
					if(user.id === id) {
						return user;
					}
				}
				return null;
			}
			
			// When the user clicks anywhere outside of the modal, close it
			window.onclick = function(event) {
			    if (event.target.id === "id_modal_user" || event.target.id === "id_modal_template" || event.target.id === "id_modal_app") {
			        $scope.modal_style_user = {"display" : "none"};
			        $scope.modal_style_template = {"display" : "none"};
			        $scope.modal_style_app = {"display" : "none"};
			        console.log($scope.user);
			        $scope.user.name = $scope.user.oldname;
			        $scope.$apply();
			    }
			}
			
			$scope.openUpdateUser = function(id) {
				var user = getUser(id)
				console.log("openUpdateUser", id, user);
				$scope.modal_style_user = {"display" : "block"};
				$scope.user = user;
				$scope.user.oldname = user.name;
			}
			$scope.updateUser = function(id) {
				$scope.modal_style_user = {"display" : "none"};
				console.log("update user", id);
				sendRequest('/updateUser', null, {"id":id, "new_name":$scope.user.name});
			}
			$scope.deleteUser = function(id) {
				console.log("deleteUser", id);
			}
			$scope.openCreateTemplate = function(id) {
				console.log("openCreateTemplate", id);
				$scope.modal_style_template = {"display" : "block"};
			}
			$scope.openUpdateTemplate = function(id) {
				console.log("openUpdateTemplate", id);
				$scope.modal_style_template = {"display" : "block"};
			}
			$scope.deleteTemplate = function(id) {
				console.log("deleteTemplate", id);
			}
			$scope.openUpdateApp = function(id) {
				console.log("openUpdateApp", id);
				$scope.modal_style_app = {"display" : "block"};
			}
			$scope.deleteApp = function(id) {
				console.log("deleteApp", id);
			}
			sendRequest('/getUsers', getUsers);
			sendRequest('/getTemplates', getTemplates);
		}]
	}
);

