//
//  CarouselViewManager.swift
//  ad
//
//  Created by Sergei Golishnikov on 19/09/2021.
//

import Foundation
import UIKit
import React

class UICell: UICollectionViewCell {
  override func layoutSubviews() {
    super.layoutSubviews()
    contentView.clipsToBounds = true
    if contentView.subviews.count > 0 {
      contentView.subviews[0].frame = .init(origin: .zero, size: frame.size)
    }
  }
  
  override func prepareForReuse() {
    super.prepareForReuse()
    contentView.subviews.forEach {
      $0.removeFromSuperview()
    }
  }
}

@objc
class _CarouselView: RCTView, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
  private var indexOfCellBeforeDragging = 0
  let layout = CarouselNonAnimatedFlowLayout()
  
  var data: [UIView] = []
  lazy var collectionView: UICollectionView = {
    layout.minimumLineSpacing = 0
    layout.minimumInteritemSpacing = 0
    layout.scrollDirection = .horizontal
    let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
    cv.isPagingEnabled = true
    cv.decelerationRate = .fast
    cv.showsHorizontalScrollIndicator = false
    cv.register(UICell.self, forCellWithReuseIdentifier: "UICell")
    cv.dataSource = self
    cv.backgroundColor = .clear
    cv.delegate = self
    return cv
  }()
  
  @objc
  var currentItemHorizontalMargin: NSNumber = 0 {
    didSet {
      layout.minLineSpacing = CGFloat(currentItemHorizontalMargin.int64Value)
    }
  }
  
  @objc
  var nextItemVisible: NSNumber = 0 {
    didSet {
      layout.minLineSpacing = CGFloat(nextItemVisible.int64Value)
    }
  }
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    addSubview(collectionView)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func layoutSubviews() {
    super.layoutSubviews()
    collectionView.frame = .init(origin: .zero, size: frame.size)
  }
  
  override func addSubview(_ view: UIView) {
    if view is UICollectionView {
      super.addSubview(view)
    } else {
      data.append(view)
    }
  }
  
  func collectionView(
    _ collectionView: UICollectionView,
    numberOfItemsInSection section: Int
  ) -> Int {
    return data.count
  }

  func collectionView(
    _ collectionView: UICollectionView,
    cellForItemAt indexPath: IndexPath
  ) -> UICollectionViewCell {
    let cell = collectionView.dequeueReusableCell(
        withReuseIdentifier: "UICell",
        for: indexPath
    ) as! UICell
    cell.contentView.addSubview(data[indexPath.row])
    return cell
  }
}

@objc(CarouselView)
class CarouselViewManager: RCTViewManager {
  override class func requiresMainQueueSetup() -> Bool {
    true
  }
  
  override func view() -> UIView! {
    _CarouselView()
  }
}
