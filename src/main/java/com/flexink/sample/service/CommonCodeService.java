package com.flexink.sample.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexink.common.domain.BaseService;
import com.flexink.domain.code.CommonCode;
import com.flexink.domain.code.CommonCodeId;
import com.flexink.domain.code.CommonCodeRepository;
import com.flexink.domain.code.QCommonCode;
import com.flexink.sample.mapper.CommonCodeMapper;
import com.flexink.vo.ParamsVo;
import com.querydsl.core.BooleanBuilder;

@Service
public class CommonCodeService extends BaseService<CommonCode, CommonCodeId> {

    private QCommonCode qCommonCode = QCommonCode.commonCode;
    
    @Autowired
    private CommonCodeMapper commonCodeMapper;

    @Autowired
    public CommonCodeService(CommonCodeRepository basicCodeRepository) {
        super(CommonCode.class, basicCodeRepository);
    }

    
    public Page<CommonCode> get(ParamsVo paramsVo) {
    	
    	paramsVo.addSort("groupCd", Sort.Direction.ASC);
        paramsVo.addSort("sort", Sort.Direction.ASC);

        String filter = paramsVo.getString("filter");
        
        BooleanBuilder builder = new BooleanBuilder();
        
        if (StringUtils.isNotEmpty(filter)) {
            builder.and(qCommonCode.groupCd.like("%"+filter+"%")
            		.or(qCommonCode.groupNm.like("%"+filter+"%"))
            		.or(qCommonCode.code.like("%"+filter+"%"))
            		.or(qCommonCode.name.like("%"+filter+"%")));
        }
        
        
        // NATIVE QUERY
        //List<CommonCode> list = getEntityManager().createNativeQuery("select * from common_code_m where group_cd = 'user'", CommonCode.class).getResultList();
        
        // JPQL
        //List<CommonCode> list2 = getEntityManager().createQuery("SELECT C FROM COMMON_CODE_M C WHERE C.GROUP_CD = 'USER'", CommonCode.class).getResultList();
        
        // Criteria
        /*CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CommonCode> cQuery = cb.createQuery(CommonCode.class);
        Root<CommonCode> c = cQuery.from(CommonCode.class);
        CriteriaQuery<CommonCode> cQuery2 = cQuery.select(c).where(cb.equal(c.get("group_cd"), "user"));
        List<CommonCode> list3 = cQuery2.createQuery(cQuery2).getResualtList();*/

        // Spring Data Jpa
        //List<CommonCode> list4 = repository.findByGroupCdEquals("user");
        
        // QueryDSL
        //List<CommonCode> list5 = queryDsl().from(qCommonCode).where(qCommonCode.groupCd.eq("user")).fetch();
        
        // Mybatis
        //List<EgovMap> list6 = commonCodeMapper.readPage(paramsVo);
        
        //List<Tuple> result = queryDsl().select(qCommonCode.data1, qCommonCode.data2).from(qCommonCode).fetch();
        
        Page<CommonCode> list = readPage(queryDsl().from(qCommonCode).where(builder), paramsVo.getPageable());
        return list;
    }

    @Transactional
    public void saveCommonCode(List<CommonCode> basicCodes) {
        save(basicCodes);
    }
}