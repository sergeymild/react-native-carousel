import React from 'react';
import {
  Platform,
  requireNativeComponent,
  StyleProp,
  ViewStyle,
} from 'react-native';
import ViewPager from 'react-native-pager-view';

interface Props {
  readonly currentItemHorizontalMargin?: number;
  readonly nextItemVisible?: number;
  readonly viewPagerStyle?: StyleProp<ViewStyle>;
  readonly style?: StyleProp<ViewStyle>;
  readonly pageMaxWidth?: number;
}

export const CarouselViewManager =
  requireNativeComponent<Props>('CarouselView');

export const CarouselPageWrapper = requireNativeComponent<{
  pageMaxWidth?: number;
}>('CarouselPageWrapper');

export const CarouselView: React.FC<Props> = (props) => {
  console.log('--', props.pageMaxWidth);
  let children =
    Platform.OS === 'ios' ? (
      props.children
    ) : (
      <ViewPager style={props.viewPagerStyle}>
        {React.Children.map(props.children, (c) => (
          <CarouselPageWrapper pageMaxWidth={props.pageMaxWidth}>
            {c}
          </CarouselPageWrapper>
        ))}
      </ViewPager>
    );

  return (
    <CarouselViewManager
      style={props.style}
      currentItemHorizontalMargin={props.currentItemHorizontalMargin}
      nextItemVisible={props.nextItemVisible}
      pageMaxWidth={props.pageMaxWidth}
    >
      {children}
    </CarouselViewManager>
  );
};
