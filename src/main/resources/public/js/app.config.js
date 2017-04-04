angular.
	module('EnterpriseJavaBasics').
	config(['$locationProvider', '$routeProvider',
		function config($locationProvider, $routeProvider) {
		$locationProvider.hashPrefix('!');

		$routeProvider.
			when('/', {
				template: '<login></login>'
			}).
			when('/login', {
				template: '<login></login>'
			}).
			when('/lauchpad', {
				template: '<lauchpad></lauchpad>'
			}).
			when('/createapp', {
				template: '<createapp></createapp>'
			}).
			when('/selectapps', {
				template: '<selectapps></selectapps>'
			}).
			when('/manager', {
				template: '<manager></manager>'
			}).
			otherwise({
		        redirectTo: '/lauchpad'
		    });
	}
	]);
