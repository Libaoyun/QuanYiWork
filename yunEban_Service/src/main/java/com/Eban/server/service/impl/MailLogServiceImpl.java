package com.Eban.server.service.impl;

import com.Eban.server.mapper.MailLogMapper;
import com.Eban.server.pojo.MailLog;
import com.Eban.server.service.IMailLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Service
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
