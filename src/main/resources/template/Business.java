package jetchart.template;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ::NAME::Business extends BaseIdBusiness<::ENTITY::, ::TYPEID::, ::NAME::Repository> {

    @Override
    protected void addSaveData(::ENTITY:: object, String ipConectada) {

    }

    @Override
    protected void addUpdateData(::ENTITY:: object, String ipConectada) {

    }

    @Override
    public void saveValidate(::ENTITY:: object, Map<String, String[]> parameters) throws BusinessException {

    }

    @Override
    public void updateValidate(::ENTITY:: object, Map<String, String[]> parameters) throws BusinessException {

    }

    @Override
    public void deleteRelationsById(::TYPEID:: id) {

    }

    @Override
    protected void saveValidateParameters(::ENTITY:: object, Map<String, String[]> parameters) throws BusinessException {

    }

    @Override
    protected void updateValidateParameters(::ENTITY:: object, Map<String, String[]> parameters) throws BusinessException {

    }

    @Override
    protected void validateBeforeDelete(::TYPEID:: id, Map<String, String[]> parameters) throws BusinessException {

    }

    @Override
    public List<CriteriaGroup> getCriteriasByFindAll(Map<String, String[]> parameters) {
        List<CriteriaGroup> criterias = new ArrayList<>();
        return criterias;
    }

}
