import { APP_INITIALIZER, ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptors, withInterceptorsFromDi } from '@angular/common/http';
import { jwtInterceptor } from './interceptors/jwt.interceptor';
import { JwtHelperService, JwtModule } from '@auth0/angular-jwt';
import { ConfigService } from './services/config.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { provideAnimations } from '@angular/platform-browser/animations';
import { rxStompServiceFactory } from './utilities/rx-stomp-service-factory';
import { RxStompService } from './services/rx-stomp.service';

export const appConfig: ApplicationConfig = {
  providers: [
    // provideHttpClient(withInterceptorsFromDi()),
    provideHttpClient(withInterceptors([jwtInterceptor])),
    {
      provide: APP_INITIALIZER,
      useFactory: configFactory,
      deps: [ConfigService],
      multi: true
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory
    },
    importProvidersFrom(
      JwtModule.forRoot({
        config: {
          tokenGetter: tokenGetter,
          allowedDomains: ["*"],
          disallowedRoutes: ["*"],
        },
      }),
      MatSnackBarModule
    ),
    provideRouter(routes),
    provideAnimations()
  ]
};

function tokenGetter(): string | Promise<string | null> | null {
  return localStorage.getItem('antimess-token')
}

function configFactory(configService: ConfigService) {
  return () => configService.loadConfig();
}

