angular.
	module('login').
	component('login', {
		templateUrl: '/js/login/login.template.html',
		controller: ["$scope",
			function LoginController($scope) {
			$scope.update = function() {
				createCookie("username", $scope.username);
			}
			
			function createCookie(name,value) {
				if(name !== undefined) {
					var expires = "",
					date = new Date();
			        date.setTime(date.getTime() + (5*24*60*60*1000));
			        expires = "; expires=" + date.toUTCString();
				    console.log("save username", $scope.username);
				    document.cookie = name + "=" + value + expires + "; path=/";
				} else {
					console.log("username not defined", $scope.username);
				}
			}
		}]
	}
);

