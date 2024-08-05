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

describe('ExtendedUser e2e test', () => {
  const extendedUserPageUrl = '/extended-user';
  const extendedUserPageUrlPattern = new RegExp('/extended-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const extendedUserSample = {};

  let extendedUser;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/extended-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/extended-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/extended-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (extendedUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/extended-users/${extendedUser.id}`,
      }).then(() => {
        extendedUser = undefined;
      });
    }
  });

  it('ExtendedUsers menu should load ExtendedUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('extended-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExtendedUser').should('exist');
    cy.url().should('match', extendedUserPageUrlPattern);
  });

  describe('ExtendedUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(extendedUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExtendedUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/extended-user/new$'));
        cy.getEntityCreateUpdateHeading('ExtendedUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', extendedUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/extended-users',
          body: extendedUserSample,
        }).then(({ body }) => {
          extendedUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/extended-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [extendedUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(extendedUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ExtendedUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('extendedUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', extendedUserPageUrlPattern);
      });

      it('edit button click should load edit ExtendedUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExtendedUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', extendedUserPageUrlPattern);
      });

      it('edit button click should load edit ExtendedUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExtendedUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', extendedUserPageUrlPattern);
      });

      it('last delete button click should delete instance of ExtendedUser', () => {
        cy.intercept('GET', '/api/extended-users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('extendedUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', extendedUserPageUrlPattern);

        extendedUser = undefined;
      });
    });
  });

  describe('new ExtendedUser page', () => {
    beforeEach(() => {
      cy.visit(`${extendedUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ExtendedUser');
    });

    it('should create an instance of ExtendedUser', () => {
      cy.get(`[data-cy="dob"]`).type('2024-08-05T01:40');
      cy.get(`[data-cy="dob"]`).blur();
      cy.get(`[data-cy="dob"]`).should('have.value', '2024-08-05T01:40');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        extendedUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', extendedUserPageUrlPattern);
    });
  });
});
