//
//  VCLoginPasswordMode.h
//  oplayer
//
//  Created by SYALON on 13-10-23.
//
//

#import <UIKit/UIKit.h>
#import "VCBase.h"

@interface VCLoginPasswordMode : VCBase<UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, UIScrollViewDelegate>

- (id)initWithOwner:(VCBase*)owner checkActivePermission:(BOOL)checkActivePermission;

@end
