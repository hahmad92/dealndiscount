import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICity } from 'app/shared/model/city.model';
import { getEntities as getCities } from 'app/entities/city/city.reducer';
import { IStore } from 'app/shared/model/store.model';
import { StoreType } from 'app/shared/model/enumerations/store-type.model';
import { StoreCategory } from 'app/shared/model/enumerations/store-category.model';
import { getEntity, updateEntity, createEntity, reset } from './store.reducer';

export const StoreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cities = useAppSelector(state => state.city.entities);
  const storeEntity = useAppSelector(state => state.store.entity);
  const loading = useAppSelector(state => state.store.loading);
  const updating = useAppSelector(state => state.store.updating);
  const updateSuccess = useAppSelector(state => state.store.updateSuccess);
  const storeTypeValues = Object.keys(StoreType);
  const storeCategoryValues = Object.keys(StoreCategory);

  const handleClose = () => {
    navigate('/store' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCities({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...storeEntity,
      ...values,
      city: cities.find(it => it.id.toString() === values.city?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          storeType: 'ONLINE',
          storeCategory: 'GROCERY',
          ...storeEntity,
          city: storeEntity?.city?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dealNdiscountApp.store.home.createOrEditLabel" data-cy="StoreCreateUpdateHeading">
            <Translate contentKey="dealNdiscountApp.store.home.createOrEditLabel">Create or edit a Store</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="store-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dealNdiscountApp.store.storeName')}
                id="store-storeName"
                name="storeName"
                data-cy="storeName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dealNdiscountApp.store.storeType')}
                id="store-storeType"
                name="storeType"
                data-cy="storeType"
                type="select"
              >
                {storeTypeValues.map(storeType => (
                  <option value={storeType} key={storeType}>
                    {translate('dealNdiscountApp.StoreType.' + storeType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('dealNdiscountApp.store.storeCategory')}
                id="store-storeCategory"
                name="storeCategory"
                data-cy="storeCategory"
                type="select"
              >
                {storeCategoryValues.map(storeCategory => (
                  <option value={storeCategory} key={storeCategory}>
                    {translate('dealNdiscountApp.StoreCategory.' + storeCategory)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('dealNdiscountApp.store.address')}
                id="store-address"
                name="address"
                data-cy="address"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dealNdiscountApp.store.phone')}
                id="store-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dealNdiscountApp.store.geoLocation')}
                id="store-geoLocation"
                name="geoLocation"
                data-cy="geoLocation"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField id="store-city" name="city" data-cy="city" label={translate('dealNdiscountApp.store.city')} type="select">
                <option value="" key="0" />
                {cities
                  ? cities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/store" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default StoreUpdate;
