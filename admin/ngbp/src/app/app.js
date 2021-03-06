angular.module( 'ngBoilerplate', [
  'templates-app',
  'templates-common',
  'ngBoilerplate.home',
  'ngBoilerplate.about',
  'ui.router'
])

.config( function myAppConfig ( $stateProvider, $urlRouterProvider ) {
  $urlRouterProvider.otherwise( '/home' );
})

.run( function run ($rootScope) {
      $rootScope.sessionData = {
        user: {
          id: 1,
          username: 'username',
          password: 'password'
        },
        isAuthenticated: false
      };
})

.controller( 'AppCtrl', function AppCtrl ( $rootScope, $scope, $location ) {
  $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
    if ( angular.isDefined( toState.data.pageTitle ) ) {
      $scope.pageTitle = toState.data.pageTitle + ' | eCommerce Admin' ;
    }
  });
})

;

