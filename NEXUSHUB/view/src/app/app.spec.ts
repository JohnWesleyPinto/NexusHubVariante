import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { signal, computed } from '@angular/core';
import { AppComponent } from './app';
import { AuthService } from './core/auth/auth.service';

describe('AppComponent', () => {
  const currentUser = signal(null);
  const authStub = {
    currentUser,
    isLoggedIn: computed(() => false),
    isAdmin: computed(() => false),
    logout: vi.fn()
  };

  beforeEach(async () => {
    Object.defineProperty(window, 'matchMedia', {
      writable: true,
      value: vi.fn().mockReturnValue({ matches: false })
    });
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authStub }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render the application shell', async () => {
    const fixture = TestBed.createComponent(AppComponent);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.app-layout')).toBeTruthy();
    expect(compiled.textContent).toContain('NEXUS');
  });
});
