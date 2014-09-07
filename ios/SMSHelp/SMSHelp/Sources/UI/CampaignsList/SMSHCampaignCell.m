//
//  SMSHCampaignCell.m
//  SMSHelp
//
//  Created by Vladimir Goranov on 6/29/14.
//  Copyright (c) 2014 Vladimir Goranov. All rights reserved.
//

#import "SMSHCampaignCell.h"
#import "SMSHDataManager.h"
#import "UIImageView+WebCache.h"

#import "SMSHCampaign.h"

CGFloat const kSMSHCampaignCellTitleFontSize = 16.0f;
CGFloat const kSMSHCampaignCellSubtitleFontSize = 17.0f;
CGFloat const kSMSHCampaignCellTitlesInterDistance = 7.0f;
CGFloat const kSMSHCampaignCellTitleWidth = 250.0f;
CGFloat const kSMSHCampaignCellAvatarWidth = 56.0f;

@interface SMSHCampaignCell ()
{
	UIImageView *_avatar;
	UILabel *_title;
	UILabel *_subtitle;
	UIImageView *_arrow;
	UIView *_separator;
}

@end

@implementation SMSHCampaignCell

+ (CGFloat) heightForCellWithCampaign:(SMSHCampaign *)campaign
{
	CGFloat h = 0.0f;
	
	CGFloat topPaddingY = normalizedLengthFromLength(12.0f);
	CGFloat bottomPaddingY = normalizedLengthFromLength(12.0f);
	
	CGFloat titlesInterDistance = normalizedLengthFromLength(kSMSHCampaignCellTitlesInterDistance);
	CGFloat avatarHeight = normalizedLengthFromLength(kSMSHCampaignCellAvatarWidth);
	
	CGFloat titleFontSize = normalizedLengthFromLength(kSMSHCampaignCellTitleFontSize);
	CGFloat titleWidth = normalizedLengthFromLength(kSMSHCampaignCellTitleWidth);
	CGFloat maxTitleHeight = 3 * [UIFont systemFontOfSize:titleFontSize].lineHeight;
	
	CGFloat subtitleFontSize = normalizedLengthFromLength(kSMSHCampaignCellSubtitleFontSize);
	
	CGSize s = [campaign.name boundingRectWithSize:cgs(titleWidth, maxTitleHeight) 
										   options:(NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading) 
										attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:titleFontSize]} 
										   context:nil].size;
	
	CGSize subtitleSize = [campaign.subname boundingRectWithSize:cgs(titleWidth, CGFLOAT_MAX) 
														 options:(NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading) 
													  attributes:@{NSFontAttributeName: [UIFont boldSystemFontOfSize:subtitleFontSize]} 
														 context:nil].size;
	
	s.height = ceilf(s.height);
	subtitleSize.height = ceilf(subtitleSize.height);
	
	if (s.height + titlesInterDistance + subtitleSize.height > avatarHeight)
	{
		h = topPaddingY + s.height + titlesInterDistance + subtitleSize.height + bottomPaddingY;
	}
	else
	{
		h = topPaddingY + avatarHeight + bottomPaddingY;
	}
	
	campaign.cellHeight = h;
	
	return h;
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self)
	{
        // Initialization code
		self.contentView.backgroundColor = [UIColor whiteColor];
		self.selectionStyle = UITableViewCellSelectionStyleNone;
		
		CGFloat paddingX = normalizedLengthFromLength(12.0f);
		
		_avatar = [[UIImageView alloc] initWithFrame:cgr(paddingX, paddingX, normalizedLengthFromLength(kSMSHCampaignCellAvatarWidth), normalizedLengthFromLength(kSMSHCampaignCellAvatarWidth))];
		_avatar.layer.cornerRadius = _avatar.height / 2.0f;
		_avatar.layer.masksToBounds = YES;
		_avatar.contentMode = UIViewContentModeScaleAspectFill;
		_avatar.backgroundColor = kSMSHColorPlaceholderImage;
		[self.contentView addSubview:_avatar];
		[_avatar release];
		
		_title = [[UILabel alloc] initWithFrame:cgr(_avatar.nextX + paddingX, 16.0f, normalizedLengthFromLength(kSMSHCampaignCellTitleWidth), 0.0f)];
		_title.textColor = kSMSHColorDarkBlue;
		_title.textAlignment = NSTextAlignmentLeft;
		_title.font = [UIFont systemFontOfSize:normalizedLengthFromLength(kSMSHCampaignCellTitleFontSize)];
		_title.backgroundColor = self.contentView.backgroundColor;
		_title.opaque = YES;
		_title.numberOfLines = 3;
		[self.contentView addSubview:_title];
		[_title release];
		
		_subtitle = [[UILabel alloc] initWithFrame:cgr(_title.originX, 0.0f, _title.width, normalizedLengthFromLength(kSMSHCampaignCellSubtitleFontSize))];
		_subtitle.textColor = kSMSHColorTextLightBlue;
		_subtitle.textAlignment = NSTextAlignmentLeft;
		_subtitle.font = [UIFont boldSystemFontOfSize:normalizedLengthFromLength(kSMSHCampaignCellSubtitleFontSize)];
		_subtitle.backgroundColor = self.contentView.backgroundColor;
		_subtitle.opaque = YES;
		[self.contentView addSubview:_subtitle];
		[_subtitle release];
		
		_arrow = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"images/disclosure_arrow"]];
		_arrow.size = _arrow.image.size;
		_arrow.endX = self.contentView.width - paddingX;
		[self addSubview:_arrow];
		[_arrow release];
		
		_separator = [[UIView alloc] initWithFrame:cgr(0.0f, 0.0f, self.contentView.boundsWidth, 0.5f)];
		_separator.backgroundColor = kSMSHColorSeparator;
		[self.contentView addSubview:_separator];
		[_separator release];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void) setCampaign:(SMSHCampaign *)campaign
{
	[_avatar sd_cancelCurrentImageLoad];
	_avatar.image = nil;
	
	if (campaign.pictureFilename.length > 0)
	{
		_avatar.image = [UIImage imageWithContentsOfFile:campaign.pictureFilename];
	}
	else
	{		
		[_avatar sd_setImageWithURL:[NSURL URLWithString:campaign.pictureUrlString] 
				   placeholderImage:nil 
							options:SDWebImageRetryFailed 
						  completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
							  if (!error)
							  {
								  [[SMSHDataManager sharedInstance] saveImage:image forCampaign:campaign];
							  }
							  else
							  {
								  DBG(@"Failed to retrieve image from network for url: %@", imageURL.absoluteString);
							  }
						  }];
	}
	
	_title.text = campaign.name;
	_subtitle.text = campaign.subname;
	
	CGSize s = [_title.text boundingRectWithSize:cgs(_title.width, 3 * _title.font.lineHeight) 
										 options:(NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading) 
									  attributes:@{NSFontAttributeName: _title.font} 
										 context:nil].size;
	_title.height = ceilf(s.height);
	
	if (_title.height + normalizedLengthFromLength(kSMSHCampaignCellTitlesInterDistance) + _subtitle.height < _avatar.height)
	{
		_title.originY = _avatar.originY + roundf((_avatar.height - (_title.height + normalizedLengthFromLength(kSMSHCampaignCellTitlesInterDistance) + _subtitle.height)) / 2.0f);
	}
	else
	{
		_title.originY = _avatar.originY;
	}
	
	_subtitle.originY = _title.nextY + normalizedLengthFromLength(kSMSHCampaignCellTitlesInterDistance);
	
	_arrow.originY = floorf((campaign.cellHeight - _arrow.height) / 2.0f);
	
	_separator.originY = campaign.cellHeight - _separator.height;
}

@end
