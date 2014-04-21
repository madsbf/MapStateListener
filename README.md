MapStateListener
================

The MapStateListener can be used to receive callbacks from GoogleMap on Android, when the map settles or unsettles, is touched or released. Settling the maps means, that the map is not scrolling, zooming or animating in any way and is not touched at the same time.

How to use it
================
You can use the MapStateListener by replacing your MapFragment with the included TouchableMapFragment, which allows for receiving touch events from the Map.
With a reference to your TouchableMapFragment and the GoogleMap-object, in your Activity, you can simply use the MapStateListener like this:

```java
new MapStateListener(mMap, mMapFragment, this) {
  @Override
  public void onMapTouched() {
    // Map touched
  }

  @Override
  public void onMapReleased() {
    // Map released
  }

  @Override
  public void onMapUnsettled() {
    // Map unsettled
  }

  @Override
  public void onMapSettled() {
    // Map settled
  }
};
```
