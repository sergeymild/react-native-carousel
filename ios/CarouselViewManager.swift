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
            let tag = self.contentView.subviews[0].reactTag
            let view = self.contentView.subviews[0]
            let size = frame.size
            RCTExecuteOnUIManagerQueue {
                if RCTBridge.current().uiManager.shadowView(forReactTag: tag) != nil {
                    RCTExecuteOnMainQueue {
                        RCTBridge.current().uiManager.setSize(size, for: view)
                        self.setNeedsLayout()
                        self.layoutIfNeeded()
                    }
                }
            }
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
    let layout = CarouselNonAnimatedFlowLayout()
    var currentPage = 0
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
    var onPageSelected: RCTDirectEventBlock?

    @objc
    var currentItemHorizontalMargin: NSNumber = 0 {
        didSet {
            layout.minLineSpacing = CGFloat(currentItemHorizontalMargin.int64Value)
        }
    }

    @objc
    var nextItemVisible: NSNumber = 0 {
        didSet {
            layout.cellOffset = CGFloat(nextItemVisible.int64Value)
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

    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let x = scrollView.contentOffset.x
        let w = scrollView.bounds.size.width
        let page = Int(ceil(x/w))
        if page == currentPage { return }
        currentPage = page
        self.onPageSelected?(["position": currentPage])
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
