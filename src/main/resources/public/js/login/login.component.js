angular.
	module('login').
	component('login', {
		templateUrl: '/js/login/login.template.html',
		controller: ["$scope",
			function LoginController($scope) {
			$scope.login_button = "login_button";
			$scope.login_input_pw = "login_input_text";
			$scope.login_input_name = "login_input_text";
			$scope.password = "";
			$scope.username = "";
			var pw_ready = false,
			    name_ready = false;
			
			$scope.$watch('username', function(){
				if($scope.username === "" || $scope.username.length > 3) {
					$scope.login_input_name = "login_input_text";
					name_ready = true;
				} else {
					$scope.login_input_name = "login_input_text_invalide";
					name_ready = false
				}
				buttonCheck();
			});
			
			$scope.$watch('password', function(){
				if($scope.password === "" || $scope.password.length > 3) {
					$scope.login_input_pw = "login_input_text";
					pw_ready = true;
				} else {
					$scope.login_input_pw = "login_input_text_invalide";
					pw_ready = false;
				}
				buttonCheck();
			});
			
			function buttonCheck() {
				if(pw_ready && name_ready && $scope.password !== "" && $scope.username !== "") {
					$scope.login_button = "login_button";
				} else {
					$scope.login_button = "login_button_invalide";
				}
				window.saveApply($scope);
			}
			
			$scope.login = function() {
			    // click login button
				createCookie("username", $scope.username);
				loginRequest();
			}

			$scope.register = function() {
			    // click register button
				createCookie("username", $scope.username);
				registerRequest();
			}

			function createCookie( name, value ) {
				if( name && value ) {
					date = new Date();
			        date.setTime(date.getTime() + ( 432000000 ) );
			        var expires = "; expires=" + date.toUTCString();
				    console.log( "save cookie; username: ", value );
				    document.cookie = name + "=" + value + expires + "; path=/";
				} else {
					console.log( "username not defined", $scope.username );
				}
			}

			function registerRequest() {
			console.log( "register START" );
			window.sendRequest('/register', function(response){
                const request = response.srcElement;
                console.log( "registerRequest", request );
                if (request.readyState === XMLHttpRequest.DONE) {
                    console.log( "registerRequest readyState", request );
                }
            }, {password:$scope.password} );}

            function loginRequest() {
            console.log( "login START" );
            window.sendRequest('/login',
             function(response){
                var request = response.srcElement;
                if (request.readyState === XMLHttpRequest.DONE) {
                    console.log( "loginRequest", request );
                }
            }, {password:$scope.password} );}
		}]
	}
);

 