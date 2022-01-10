// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import { HttpHeaders } from '@angular/common/http';

export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  httpOptions: {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  },
  oauthClientId: 'client-project-template',
  oauthLoginUrl: 'http://localhost:9005/auth/realms/elca/protocol/openid-connect/auth',
  oauthUrl: "http://localhost:9005/auth/realms/elca/protocol/openid-connect/",
  oauthTokenUrl: 'http://localhost:9005/auth/realms/elca/protocol/openid-connect/token',
  oauthCallbackUrl: 'http://localhost:4200/auth',
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
