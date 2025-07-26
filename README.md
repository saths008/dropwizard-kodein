# dropwizard-kodein

Library for easy integration between Kodein and dropwizard.
Takes inspiration from [xvik/dropwizard-guicey](https://github.com/xvik/dropwizard-guicey)



## TODO
- Auto class scanning and registering using Kodein instances
- We will have a list of default installers, which can be disabled if needed.
  - Create ResourceInstaller:
    > For example, ResourceInstaller will:
    >
    > recognize MyResource class as rest resource by @Path annotation
    > gets instance from injector (injector.getInstance(MyResource.class)) and performs registration environment.jersey().register(guiceManagedInstance)
  - Create FilterInstaller
- @Order annotation for installer order
- Invisible to scanner annotation
- Make dropwizard BOM