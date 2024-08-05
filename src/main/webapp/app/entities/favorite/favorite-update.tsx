import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFavorite } from 'app/shared/model/favorite.model';
import { FavoriteType } from 'app/shared/model/enumerations/favorite-type.model';
import { getEntity, updateEntity, createEntity, reset } from './favorite.reducer';

export const FavoriteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const favoriteEntity = useAppSelector(state => state.favorite.entity);
  const loading = useAppSelector(state => state.favorite.loading);
  const updating = useAppSelector(state => state.favorite.updating);
  const updateSuccess = useAppSelector(state => state.favorite.updateSuccess);
  const favoriteTypeValues = Object.keys(FavoriteType);

  const handleClose = () => {
    navigate('/favorite' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...favoriteEntity,
      ...values,
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
          favoriteType: 'STORE',
          ...favoriteEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dealNdiscountApp.favorite.home.createOrEditLabel" data-cy="FavoriteCreateUpdateHeading">
            <Translate contentKey="dealNdiscountApp.favorite.home.createOrEditLabel">Create or edit a Favorite</Translate>
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
                  id="favorite-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dealNdiscountApp.favorite.userId')}
                id="favorite-userId"
                name="userId"
                data-cy="userId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dealNdiscountApp.favorite.storeId')}
                id="favorite-storeId"
                name="storeId"
                data-cy="storeId"
                type="text"
              />
              <ValidatedField
                label={translate('dealNdiscountApp.favorite.cityId')}
                id="favorite-cityId"
                name="cityId"
                data-cy="cityId"
                type="text"
              />
              <ValidatedField
                label={translate('dealNdiscountApp.favorite.cardId')}
                id="favorite-cardId"
                name="cardId"
                data-cy="cardId"
                type="text"
              />
              <ValidatedField
                label={translate('dealNdiscountApp.favorite.favoriteType')}
                id="favorite-favoriteType"
                name="favoriteType"
                data-cy="favoriteType"
                type="select"
              >
                {favoriteTypeValues.map(favoriteType => (
                  <option value={favoriteType} key={favoriteType}>
                    {translate('dealNdiscountApp.FavoriteType.' + favoriteType)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/favorite" replace color="info">
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

export default FavoriteUpdate;
