import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './card.reducer';

export const CardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cardEntity = useAppSelector(state => state.card.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cardDetailsHeading">
          <Translate contentKey="dealNdiscountApp.card.detail.title">Card</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cardEntity.id}</dd>
          <dt>
            <span id="cardName">
              <Translate contentKey="dealNdiscountApp.card.cardName">Card Name</Translate>
            </span>
          </dt>
          <dd>{cardEntity.cardName}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="dealNdiscountApp.card.category">Category</Translate>
            </span>
          </dt>
          <dd>{cardEntity.category}</dd>
          <dt>
            <span id="cardNetwork">
              <Translate contentKey="dealNdiscountApp.card.cardNetwork">Card Network</Translate>
            </span>
          </dt>
          <dd>{cardEntity.cardNetwork}</dd>
          <dt>
            <Translate contentKey="dealNdiscountApp.card.bank">Bank</Translate>
          </dt>
          <dd>{cardEntity.bank ? cardEntity.bank.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/card" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/card/${cardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CardDetail;
