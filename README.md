# AsyncImageLoaderLib
Implemented library and  application having following functionality,

- Data from http://pastebin.com/raw/wgkJgazE   is used in the sample application
- Image and JSON both cached in memory only i.e. no disk cache
- The size of the cache can change according to the client who uses the library and automatically removal of least used entry if size is full.
- While loading image it can be canceled without affecting the other loading.
- The request is independent of each other and same url will return the same resource.
- Through the library, a client can request for any number of distinct parallel request.
- Easily integrated with new Android project/application. 
- The structure of library can be changed when requirements come, because of its solid structure and design patterns.
- In the Sample application, it loads 10 items at a time, when it reach the end of the list will load next item. Animation added while scrolling up or down in RecyclerView.
- Material UI elements added in Sample application(i.e. Ripple effect, Material buttons, Animations, fragment shared element transition etc ).
- Pull to refresh functionality is added with recycler view, which will refresh the data set.
