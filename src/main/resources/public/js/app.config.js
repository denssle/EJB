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
			otherwise({
		        redirectTo: '/'
		    });
	}
	]);
	