# IMat
Authentication:
  ShoppingState:
    + boolean isAuthenticated()
    + void authenticate(String username, String password)
    + void unAuthenticate()
    + Optional<User> getAuthenticatedUser()
