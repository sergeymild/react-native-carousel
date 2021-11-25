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
  readonly onPageSelect?: (pageIndex: number) => void;
}

export const CarouselViewManager =
  requireNativeComponent<Props>('CarouselView');

export const CarouselPageWrapper = requireNativeComponent<{
  pageMaxWidth?: number;
}>('CarouselPageWrapper');

export const CarouselView: React.FC<Props> = (props) => {
  let children =
    Platform.OS === 'ios' ? (
      props.children
    ) : (
      <ViewPager
        style={props.viewPagerStyle}
        onPageSelected={
          props.onPageSelect
            ? (e) => props.onPageSelect?.(e.nativeEvent.position)
            : undefined
        }
      >
        {React.Children.map(props.children, (c) => (
          <CarouselPageWrapper>{c}</CarouselPageWrapper>
        ))}
      </ViewPager>
    );

  return (
    <CarouselViewManager
      style={props.style}
      currentItemHorizontalMargin={props.currentItemHorizontalMargin}
      nextItemVisible={props.nextItemVisible}
      pageSelect={
        props.onPageSelect
          ? (e) => props.onPageSelect?.(e.nativeEvent.pageIndex)
          : undefined
      }
    >
      {children}
    </CarouselViewManager>
  );
};
