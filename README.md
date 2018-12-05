# 属性说明
| vcvItemCount | 验证码位数 |
| --- | --- | --- |
| vcvItemWidth | 单个验证码框宽度 |
| vcvItemHeight | 单个验证码框高度 |
| vcvItemDistance | 两个验证码框之间的距离 |
| vcvBorderWidth | 验证码框的宽度 |
| vcvBorderStyle | 验证码框的样式(矩形，线) |
| vcvBorderRadius | 框为矩形时候的圆角 |
| vcvContentStatus  | 验证码的显示状态(明文，密码) |
| vcvContentSize | 验证码为明文时的文本大小 |
| vcvContentRadiusWhenIsHidden | 验证码为密文时候的圆半径 |
| vcvCompletedBorderColor | 已填写内容的边框颜色 |
| vcvCompletedContentColor | 已填写的文本颜色 |
| vcvUnCompleteBorderColor | 未填写内容的边框颜色 |
| vcvIsAheadDraw | 是否提前渲染一个框 |
# 效果图
## vcvIsAheadDraw
　　vcvIsAheadDraw表示是否提前渲染一个框，默认为false，效果图如下所示。
</ br>
![isAheadDrawFalse](/images/isAheadDrawFalse.gif)
　　如果设置vcvIsAheadDraw为true，效果图如下所示。
</ br>
![isAheadDrawTrue](/images/isAheadDrawTrue.gif)
## vcvBorderStyle
　　vcvBorderStyle表示边框样式，提供两种边框样式，一种是RECTANGLE，矩形，当边框样式为矩形的时候，可以通过vcvBorderRadius属性来改变矩形的圆角，效果图如下所示。
</ br>
![borderStyleRectangle](/images/borderStyleRectangle.png)
　　另外一种样式是线，效果图如下所示。
</ br>
![borderStyleLine](/images/borderStyleLine.png)
## vcvContentStatus
　　vcvContentStatus表示输入验证码的状态，是作为明文还是作为密文，当设置为SHOW的时候，表示验证码会以明文的形式展示，可以通过vcvContentSize属性来改变文本的大小，效果图如下所示。
</ br>
![contentStatusShow](/images/contentStatusShow.gif)
　　当设置为HIDE的时候，表示验证码会以密文的形式展示，可以通过vcvContentRadiusWhenIsHidden属性来设置密文时候圆的半径，效果图如下。
</ br>
![contentStatusHide](/images/contentStatusHide.gif)
# 输入监听
　　当验证码输入完成的时候，如果控件设置有OnInputCompletedListener，会自动回调onInputCompleted()方法。
```Java
public interface OnInputCompletedListener {
    void onInputCompleted(String input);
}
```


