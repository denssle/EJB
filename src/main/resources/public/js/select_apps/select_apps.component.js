angular.
	module('select_apps').
	component('select_apps', {
		templateUrl: '/js/select_apps/select_apps.template.html',
		controller: [ 'SelectAppsController',
			function($scope) {
				console.log("hello select_apps");
		}]
	});