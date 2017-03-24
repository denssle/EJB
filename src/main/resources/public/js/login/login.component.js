angular.
	module('login').
	component('login', {
		templateUrl: '/js/login/login.template.html',
		controller: ["$scope",
			function LoginController($scope) {
			localStorage.setItem("username", $scope.username);
			$scope.update = function() {
				console.log("save username", $scope.username);
				localStorage.setItem("username", $scope.username);
			}
		}]
	});