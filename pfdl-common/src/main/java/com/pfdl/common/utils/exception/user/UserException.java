package com.pfdl.common.utils.exception.user;


import com.pfdl.common.utils.exception.BaseException;

/**
 * 用户信息异常类
 * 
 * @author zhaoyt
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
