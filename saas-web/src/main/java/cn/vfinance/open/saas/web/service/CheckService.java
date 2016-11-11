package cn.vfinance.open.saas.web.service;

import cn.vfinance.open.saas.web.dao.MerchantInfoMapper;
import cn.vfinance.open.saas.web.enums.MerchantActiveStatus;
import cn.vfinance.open.saas.web.model.MerchantInfo;
import cn.vfinance.open.saas.web.util.CheckUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qiuwei on 2016/11/3.
 */
@Service
public class CheckService {

    @Autowired
    private MerchantInfoMapper merchantInfoMapper;

    /**
     * 检查失效
     * @return
     */
    public MerchantInfo checkValid(String id, String createTime) {
        MerchantInfo merchantInfo = merchantInfoMapper.selectByPrimaryKey(Integer.parseInt(id));
        if(merchantInfo ==null || createTime.equals(merchantInfo.getCreateTime().getTime())){
            return null;
        }
        return merchantInfo;
    }

    public boolean checkActive(MerchantInfo merchantInfo) {
        if(MerchantActiveStatus.ACTIVE.getStatus().equals(merchantInfo.getStatus())){
            return false;
        }
        return true;
    }

    public void checkMerchant(String id, String email, String createTime) throws Exception {
        MerchantInfo merchantInfo = merchantInfoMapper.selectByPrimaryKey(Integer.parseInt(id));
        if (merchantInfo == null) {
            throw new Exception("账户不存在");
        }
        if(!email.equals(merchantInfo.getEmail())){
            throw new Exception("账户与邮箱不匹配");
        }
    }


    public void checkInput(String id, String pwd, String rePwd, String email,String createTime) throws Exception {

        if(StringUtils.isBlank(id)){
            throw new Exception("账户编号为空，请联系管理员重新激活");
        }
		/* 非空判断 */
        if (StringUtils.isBlank(email)) {
            throw new Exception("邮箱不能为空");
        }

        if (StringUtils.isBlank(pwd)) {
            throw new Exception("密码不能为空");
        }

        if(StringUtils.isBlank(rePwd)){
            throw new Exception("确认密码不能为空");
        }

		/* 检查密码和邮箱格式 */
        if(!CheckUtils.checkEmail(email)){
            throw new Exception("邮箱格式不正确");
        }

        if(!CheckUtils.checkPassword(pwd)){
            throw new Exception("密码格式不符");
        }

        if(!pwd.equals(rePwd)){
            throw new Exception("两次密码不一致");
        }

        if(StringUtils.isBlank(createTime)){
            throw new Exception("激活邮箱时间戳丢失");
        }

    }
}
