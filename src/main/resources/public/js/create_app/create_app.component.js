angular.
	module('create_app').
	component('create_app', {
		templateUrl: '/js/create_app/create_app.template.html',
		controller: [
			function CreateAppController() {
				console.log("hello create app");
		}]
	});