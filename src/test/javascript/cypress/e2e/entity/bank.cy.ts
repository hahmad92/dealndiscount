import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Bank e2e test', () => {
  const bankPageUrl = '/bank';
  const bankPageUrlPattern = new RegExp('/bank(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const bankSample = { bankName: 'beneath', bankType: 'CONVENTIONAL' };

  let bank;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/banks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/banks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/banks/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (bank) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/banks/${bank.id}`,
      }).then(() => {
        bank = undefined;
      });
    }
  });

  it('Banks menu should load Banks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bank');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Bank').should('exist');
    cy.url().should('match', bankPageUrlPattern);
  });

  describe('Bank page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bankPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Bank page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bank/new$'));
        cy.getEntityCreateUpdateHeading('Bank');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/banks',
          body: bankSample,
        }).then(({ body }) => {
          bank = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/banks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/banks?page=0&size=20>; rel="last",<http://localhost/api/banks?page=0&size=20>; rel="first"',
              },
              body: [bank],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(bankPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Bank page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bank');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);
      });

      it('edit button click should load edit Bank page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Bank');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);
      });

      it('edit button click should load edit Bank page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Bank');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);
      });

      it('last delete button click should delete instance of Bank', () => {
        cy.intercept('GET', '/api/banks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('bank').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);

        bank = undefined;
      });
    });
  });

  describe('new Bank page', () => {
    beforeEach(() => {
      cy.visit(`${bankPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Bank');
    });

    it('should create an instance of Bank', () => {
      cy.get(`[data-cy="bankName"]`).type('geez');
      cy.get(`[data-cy="bankName"]`).should('have.value', 'geez');

      cy.get(`[data-cy="bankType"]`).select('CONVENTIONAL');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        bank = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', bankPageUrlPattern);
    });
  });
});
