angular.
	module('lauchpad').
	component('lauchpad', {
		templateUrl: '/js/lauchpad/lauchpad.template.html',
		controller: ['$routeParams',
			function LauchpadController($routeParams) {
			console.log('$routeParams:', $routeParams);
	    }
		]
	});
