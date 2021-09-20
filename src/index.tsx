import { requireNativeComponent, ViewStyle } from 'react-native';

type CarouselProps = {
  color: string;
  style: ViewStyle;
};

export const CarouselViewManager = requireNativeComponent<CarouselProps>(
'CarouselView'
);

export default CarouselViewManager;
