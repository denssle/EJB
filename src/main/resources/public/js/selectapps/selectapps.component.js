angular.
	module('selectapps').
	component('selectapps', {
		templateUrl: '/js/selectapps/selectapps.template.html',
		controller: [ '$scope',
			function SelectAppsController($scope) {
				console.log("hello select_apps");
		}]
	});

