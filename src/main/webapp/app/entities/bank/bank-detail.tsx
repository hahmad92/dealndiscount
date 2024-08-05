import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bank.reducer';

export const BankDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bankEntity = useAppSelector(state => state.bank.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bankDetailsHeading">
          <Translate contentKey="dealNdiscountApp.bank.detail.title">Bank</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bankEntity.id}</dd>
          <dt>
            <span id="bankName">
              <Translate contentKey="dealNdiscountApp.bank.bankName">Bank Name</Translate>
            </span>
          </dt>
          <dd>{bankEntity.bankName}</dd>
          <dt>
            <span id="bankType">
              <Translate contentKey="dealNdiscountApp.bank.bankType">Bank Type</Translate>
            </span>
          </dt>
          <dd>{bankEntity.bankType}</dd>
        </dl>
        <Button tag={Link} to="/bank" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bank/${bankEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BankDetail;
