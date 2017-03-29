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
			when('/create_app', {
				templateUrl: 'js/create_app/create_app.template.html',
		        controller: 'CreateAppController'
			}).
			when('/select_apps', {
				templateUrl: 'js/select_apps/select_apps.template.html',
		        controller: 'SelectAppsController'
			}).
			otherwise({
		        redirectTo: '/lauchpad'
		    });
	}
	]);
	