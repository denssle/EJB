angular.
	module('manager').
	component('manager', {
		templateUrl: '/js/manager/manager.template.html',
		controller: ["$scope",
			function managerController($scope) {
			var self = this;
			self.users=[];
			self.tenplates = [];
			
			function saveApply() {
				if ($scope.$root.$$phase !== '$apply' && $scope.$root.$$phase !== '$digest') {
					console.log("update!");
				    $scope.$apply();
				} else {
					console.log("Snap!");
				}
			}
			
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
						saveApply();
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
						saveApply();
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
			
			function getTemplate(id) {
				for(var i=0; i<self.templates.length; i++) {
					var template = self.templates[i];
					if(template.id === id) {
						return template;
					}
				}
				return null;
			}
			
			function getApp(id) {
				for(var i=0; i<self.templates.length; i++) {
					var template = self.templates[i];
					for(var j=0; j<template.apps.length; j++) {
						var app = template.apps[j];
						if(app.id === id) {
							return app;
						}
					}
				}
				return null;
			}
			
			// When the user clicks anywhere outside of the modal, close it
			window.onclick = function(event) {
			    if (event.target.className === "modal_wrapper") {
			        $scope.modal_style_user_create = {"display" : "none"};
			        $scope.modal_style_user_update = {"display" : "none"};
			        $scope.modal_style_template_create = {"display" : "none"};
			        $scope.modal_style_template_update = {"display" : "none"};
			        $scope.modal_style_app_update = {"display" : "none"};
			        if($scope.updateUserVar) {
			        	$scope.updateUserVar.name = $scope.updateUserVar.oldname;
			        }
			        if($scope.updateTemplateVar) {
			        	$scope.updateTemplateVar.name = $scope.updateTemplateVar.oldname;
			        }
			        if($scope.updateAppVar) {
			        	$scope.updateAppVar.name = $scope.updateAppVar.oldapp.name;
			        	$scope.updateAppVar.description = $scope.updateAppVar.oldapp.description;
			        	$scope.updateAppVar.url = $scope.updateAppVar.oldapp.url;
			        	$scope.updateAppVar.template = $scope.updateAppVar.oldapp.template;
			        }
			        saveApply();
			    }
			}
			// USER STUFF
			$scope.openCreateUser = function(id) {
				console.log("openCreateUser", id);
				$scope.modal_style_user_create = {"display" : "block"};
			}
			$scope.createUser = function() {
				console.log("createUser", $scope.createUserName, $scope.createUserPassword);
				$scope.modal_style_user_create = {"display" : "none"};
				sendRequest('/createUser', function(response){
					var request = response.srcElement;
					if (request.readyState === XMLHttpRequest.DONE) {
						if (request.status === 200) {
							sendRequest('/getUsers', getUsers);
						}
					}
				}, {newUserName:$scope.createUserName, newUserPassword:$scope.createUserPassword});
			}
			$scope.openUpdateUser = function(id) {
				var user = getUser(id)
				console.log("openUpdateUser", id, user);
				$scope.modal_style_user_update = {"display" : "block"};
				$scope.updateUserVar = user;
				$scope.updateUserVar.oldname = user.name;
			}
			$scope.updateUser = function(id) {
				$scope.modal_style_user_update = {"display" : "none"};
				console.log("update user", id);
				sendRequest('/updateUser', null, {"id":id, "new_name":$scope.updateUserVar.name});
				$scope.updateUserVar.oldname = $scope.updateUserVar.name;
			}
			$scope.deleteUser = function(id) {
				console.log("deleteUser", id);
				sendRequest('/deleteUser', null, {"id":id});
				for(var i=0; i<self.users.length; i++) {
					if(self.users[i].id === id) {
						self.users.splice(i, 1);
					}
				}
				saveApply();
			}
			//TEMPLATES
			$scope.openCreateTemplate = function(id) {
				console.log("openCreateTemplate", id);
				$scope.modal_style_template_create = {"display" : "block"};
			}
			$scope.createTemplate = function() {
				console.log("createTemplate", $scope.createTemplateName);
				$scope.modal_style_template_create = {"display" : "none"};
				sendRequest('/createTemplate', function(response){
					var request = response.srcElement;
					if (request.readyState === XMLHttpRequest.DONE) {
						if (request.status === 200) {
							sendRequest('/getTemplates', getTemplates);
						}
					}
				}, {newTemplateName:$scope.createTemplateName});
			}
			$scope.openUpdateTemplate = function(id) {
				var template = getTemplate(id);
				console.log("openUpdateTemplate", id, template);
				$scope.modal_style_template_update = {"display" : "block"};
				$scope.updateTemplateVar = template;
				$scope.updateTemplateVar.oldname = template.name;
			}
			$scope.updateTemplate = function(id) {
				console.log("updateTemplate", id);
				$scope.modal_style_template_update = {"display" : "none"};
				sendRequest('/updateTemplate', null, {"id":id, "new_name":$scope.updateTemplateVar.name});
				$scope.updateTemplateVar.oldname = $scope.updateTemplateVar.name;
			}
			$scope.deleteTemplate = function(id) {
				console.log("deleteTemplate", id);
				sendRequest('/deleteTemplate', null, {"id":id});
				for(var i=0; i<self.templates.length; i++) {
					if(self.templates[i].id === id) {
						self.templates.splice(i, 1);
					}
				}
			}
			// APPS
			$scope.openUpdateApp = function(id) {
				var app = getApp(id);
				console.log("openUpdateApp");
				$scope.modal_style_app_update = {"display" : "block"};
				$scope.updateAppVar = app;
				$scope.updateAppVar.oldapp = {};
				for(var key in app) {
					if(app.hasOwnProperty(key)) {
						$scope.updateAppVar.oldapp[key] = app[key];
					}
				}
				
			}
			$scope.updateApp = function(id) {
				console.log("updateApp", id, $scope.updateAppTemplate, $scope.updateAppTemplate);
				var app = $scope.updateAppVar;
				$scope.modal_style_app_update = {"display" : "none"};
				sendRequest('/updateApp', function(response){
					var request = response.srcElement;
					if (request.readyState === XMLHttpRequest.DONE) {
						if (request.status === 200) {
							sendRequest('/getTemplates', getTemplates);
						}
					}
				}, {
					id:id,
					name: app.name,
					description: app.description,
					url: app.url,
					template: $scope.updateAppTemplate.id
					});
				$scope.updateAppVar.oldname = $scope.updateAppVar.app;
			}
			$scope.deleteApp = function(id) {
				console.log("deleteApp", id);
			}
			sendRequest('/getUsers', getUsers);
			sendRequest('/getTemplates', getTemplates);
		}]
	}
);

