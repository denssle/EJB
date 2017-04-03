angular.
	module('selectapps').
	component('selectapps', {
		templateUrl: '/js/selectapps/selectapps.template.html',
		controller: [ '$scope', 
			function SelectAppsController($scope) {
			var self = this,
			scope = $scope,
			sending = true;
			scope.response = {};
			
			function sendRequest(url, key, value) {
				function getTemplatsWithApps() {
					if (request.readyState === XMLHttpRequest.DONE) {
						sending = false;
						if (request.status === 200) {
							self.response = JSON.parse(request.responseText);
							console.log("response text: ", self.response);
							if ($scope.$root.$$phase !== '$apply' && $scope.$root.$$phase !== '$digest') {
								console.log("update!");
							    $scope.$apply();
							} else {
								console.log("Snap!");
							}
						} else {
							console.log('There was a problem with the request.', request.status);
						}
					}
				}
				
				var request = new XMLHttpRequest();
				request.onreadystatechange = getTemplatsWithApps;
				request.open('POST', url, true);
				request.setRequestHeader("token", getToken("token"));
				request.setRequestHeader("username", getToken("username"));
				if(key && value) {
					request.setRequestHeader(key, value);
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
			
			function getApp(id) {
				for(var i=0; i<self.response.length; i++) {
					var template = self.response[i];
					for(var j=0; j<template.apps.length; j++) {
						var app = template.apps[j];
						if(app.id === id) {
							return app;
						}
					}
				}
				return null;
			}
			
			$scope.checkApp = function(id) {
				var app = getApp(id);
				console.log("checkApp", id, app);
				if (app !== null && !sending) {
					sending = true;
					sendRequest('/checkApp', "appid", app.id);
				} else {
					console.warn("app not found or sending", app, sending);
				}
			}
			
			$scope.checkTemplate = function(id) {
				console.log("check template", id);
				if (!sending) {
					sending = true;
					sendRequest('/checkTemplate', "templateId", id);
				} else {
					console.warn("sending error", sending);
				}
			}
			
			sendRequest('/getTemplates');
		}]
	});

