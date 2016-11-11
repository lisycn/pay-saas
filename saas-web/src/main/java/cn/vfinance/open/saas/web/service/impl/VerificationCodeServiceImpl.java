package cn.vfinance.open.saas.web.service.impl;

import cn.vfinance.open.saas.web.service.VerificationCodeService;
import cn.vfinance.open.saas.web.util.ConstantClassField;
import cn.vfinance.open.saas.web.util.ExternalConfig;
import cn.vfinance.open.saas.web.util.RandCodeImageEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Service(value = "verificationCodeService")
public class VerificationCodeServiceImpl implements VerificationCodeService {

	@Autowired
	private ExternalConfig externalConfig;

	/**
	 * 随机产生干扰线，就是前端页面显示的小点点的个数
	 */
	private static final int count = 200;
	
	/**
     * 定义图形大小(宽)
     */
	private static final int width = 105;
	
	/**
	 * 定义图形大小(高)
	 */
	private static final int height = 35;
	
	/**
	 * 干扰线的长度=1.414*lineWidth
	 */
	private static final int lineWidth = 1;
	
	@Override
	public void generate(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		// 设置页面不缓存
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				
				// 在内存中创建图象
				final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				// 获取图形上下文
				final Graphics2D graphics = (Graphics2D) image.getGraphics();
				 
				// 设定背景颜色
				graphics.setColor(Color.WHITE); // ---1.Color.WHITE为白色
				graphics.fillRect(0, 0, width, height);//填充衍射
				// 设定边框颜色
				//graphics.setColor(getRandColor(100, 200)); // ---2.这是以数字型来设置颜色，颜色模式是指使用三种基色：红、绿、蓝，通过三种颜色的调整得出其它各种颜色，这三种基色的值范围为0～255
				graphics.drawRect(0, 0, width - 1, height - 1);
				final Random random = new Random();
				// 随机产生干扰线，使图象中的认证码不易被其它程序探测到
				for (int i = 0; i < count; i++) {
				graphics.setColor(getRandColor(150, 200)); // ---3.
				 
				final int x = random.nextInt(width - lineWidth - 1) + 1; // 保证画在边框之内
				final int y = random.nextInt(height - lineWidth - 1) + 1;
				final int xl = random.nextInt(lineWidth);
				final int yl = random.nextInt(lineWidth);
				graphics.drawLine(x, y, x + xl, y + yl);
				}
				// 取随机产生的认证码(4位数字)
				final String resultCode = exctractRandCode();
				for (int i = 0; i < resultCode.length(); i++) {
				 
				 // 设置字体颜色
				graphics.setColor(Color.BLACK);
				// 设置字体样式
				graphics.setFont(new Font("Times New Roman", Font.BOLD, 24));
				// 设置字符，字符间距，上边距
				graphics.drawString(String.valueOf(resultCode.charAt(i)), (23 * i) + 8, 26);
				}
				System.out.println("直接输出："+resultCode);
				// 将认证码存入SESSION
				request.getSession().setAttribute(ConstantClassField.SESSION_KEY_OF_RAND_CODE, resultCode);
				// 图象生效
				graphics.dispose();
				 
				// 输出图象到页面
				try {
					ImageIO.write(image, "JPEG", response.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}  
	}
	
	/**
	 * @return 随机码
	 */
	private String exctractRandCode() {
		final String randCodeType = externalConfig.getRandCodeType();
	    int randCodeLength = Integer.parseInt(externalConfig.getRandCodeLength());
	    if (randCodeType == null) {
		    return RandCodeImageEnum.NUMBER_CHAR.generateStr(randCodeLength);
		} else {
			switch (randCodeType.charAt(0)) {
				case '1':
				return RandCodeImageEnum.NUMBER_CHAR.generateStr(randCodeLength);
				case '2':
				return RandCodeImageEnum.LOWER_CHAR.generateStr(randCodeLength);
				case '3':
				return RandCodeImageEnum.UPPER_CHAR.generateStr(randCodeLength);
				case '4':
				return RandCodeImageEnum.LETTER_CHAR.generateStr(randCodeLength);
				case '5':
				return RandCodeImageEnum.ALL_CHAR.generateStr(randCodeLength);
				default:
				return RandCodeImageEnum.NUMBER_CHAR.generateStr(randCodeLength);
		    }
	   }
	}
	
	/**
	 * 描述：根据给定的数字生成不同的颜色
	 * @param 这是以数字型来设置颜色，颜色模式是指使用三种基色：红、绿、蓝，通过三种颜色的调整得出其它各种颜色，这三种基色的值范围为0～255
	 * @return 描述：返回颜色
	 */
	private Color getRandColor(int fc, int bc) { // 取得给定范围随机颜色
		final Random random = new Random();
		if (fc > 255) {
		fc = 255;
		}
		if (bc > 255) {
		bc = 255;
		}
		final int r = fc + random.nextInt(bc - fc);
		final int g = fc + random.nextInt(bc - fc);
		final int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}



}
