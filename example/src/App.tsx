import * as React from 'react';

import { Image, StyleSheet, View } from 'react-native';
import { CarouselView } from 'react-native-carousel';

const banners = [
  { image: require('./assets/banners/banner1.jpeg') },
  { image: require('./assets/banners/banner2.jpeg') },
  { image: require('./assets/banners/banner3.jpeg') },
  { image: require('./assets/banners/banner4.jpeg') },
  { image: require('./assets/banners/banner5.jpeg') },
  { image: require('./assets/banners/banner6.jpeg') },
  { image: require('./assets/banners/banner7.jpeg') },
  { image: require('./assets/banners/banner8.jpeg') },
];

export default function App() {
  return (
    <View style={styles.container}>
      <CarouselView
        pageSelected={(index) => console.log('-----', index)}
        currentItemHorizontalMargin={24}
        nextItemVisible={12}
        style={{ width: '100%', height: 180 }}
        viewPagerStyle={styles.viewPager}
      >
        {banners.map((b) => (
          <Image
            key={b.image}
            source={b.image}
            style={{
              overflow: 'hidden',
              height: 180,
              width: 300,
              borderRadius: 20,
            }}
            resizeMode={'cover'}
          />
        ))}
      </CarouselView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginBottom: 16,
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },

  viewPager: {
    width: '100%',
    height: 180,
  },
});
