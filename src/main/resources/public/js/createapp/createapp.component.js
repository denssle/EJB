angular.
	module('createapp').
	component('createapp', {
		templateUrl: '/js/createapp/createapp.template.html',
		controller: ["$scope",
			function CreateAppController($scope) {
				console.log("hello create app");
		}]
	});
