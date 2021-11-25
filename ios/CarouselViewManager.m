#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(CarouselView, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(currentItemHorizontalMargin, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(nextItemVisible, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(pageSelect, RCTBubblingEventBlock)
@end
