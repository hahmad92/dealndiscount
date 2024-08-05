import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './favorite.reducer';

export const FavoriteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const favoriteEntity = useAppSelector(state => state.favorite.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="favoriteDetailsHeading">
          <Translate contentKey="dealNdiscountApp.favorite.detail.title">Favorite</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{favoriteEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dealNdiscountApp.favorite.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{favoriteEntity.userId}</dd>
          <dt>
            <span id="storeId">
              <Translate contentKey="dealNdiscountApp.favorite.storeId">Store Id</Translate>
            </span>
          </dt>
          <dd>{favoriteEntity.storeId}</dd>
          <dt>
            <span id="cityId">
              <Translate contentKey="dealNdiscountApp.favorite.cityId">City Id</Translate>
            </span>
          </dt>
          <dd>{favoriteEntity.cityId}</dd>
          <dt>
            <span id="cardId">
              <Translate contentKey="dealNdiscountApp.favorite.cardId">Card Id</Translate>
            </span>
          </dt>
          <dd>{favoriteEntity.cardId}</dd>
          <dt>
            <span id="favoriteType">
              <Translate contentKey="dealNdiscountApp.favorite.favoriteType">Favorite Type</Translate>
            </span>
          </dt>
          <dd>{favoriteEntity.favoriteType}</dd>
        </dl>
        <Button tag={Link} to="/favorite" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/favorite/${favoriteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FavoriteDetail;
