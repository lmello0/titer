# StagePass — Theater Play Review Application
### Product Requirements Document

| Field | Value |
|---|---|
| **Version** | 1.0 — MVP |
| **Date** | February 2026 |
| **Platforms** | Web, iOS, Android |
| **Status** | Draft — For Developer Review |
| **Author** | Product Team |

---

## 1. Overview

StagePass is a community-driven platform where theater enthusiasts can discover plays, write reviews, and engage with fellow theatergoers. Users can submit plays to the database, write and rate reviews, and interact with content through likes, comments, and follows. A trusted user tier ensures data quality through a verified badge system for play entries.

---

## 2. Goals & Success Metrics

### 2.1 Business Goals

- Build a high-quality, trusted database of theater plays.
- Foster an engaged community of theater reviewers.
- Differentiate from generic review platforms by focusing on theater-specific metadata and social features.

### 2.2 MVP Success Metrics

| Metric | Target (3 months post-launch) |
|---|---|
| Registered users | 1,000+ |
| Plays in database | 500+ (with 200+ verified) |
| Reviews submitted | 2,500+ |
| Avg. reviews per play | ≥ 3 |
| Trusted user verifications / week | ≥ 20 |

---

## 3. User Roles & Permissions

| Role | Description | Key Permissions |
|---|---|---|
| **Guest** | Unauthenticated visitor | Browse plays, read reviews, search |
| **Registered User** | Standard authenticated user | All of the above + submit plays, write reviews, like, comment, follow |
| **Trusted User** | Elevated role, granted by admins | All of the above + verify plays (grant verified badge), edit play metadata |
| **Admin** | Platform operator | All of the above + manage users, assign Trusted role, moderate content |

---

## 4. User Stories

### 4.1 Play Registration & Verification

| ID | As a... | I want to... | So that... |
|---|---|---|---|
| US-01 | Registered User | Submit a new play with title, author, genre, synopsis, and premiere date | Others can discover and review it |
| US-02 | Registered User | See a clear 'Unverified' label on plays not yet reviewed by a trusted user | I know the data may be incomplete |
| US-03 | Trusted User | Review a pending play submission and approve or reject it | The database maintains quality |
| US-04 | Trusted User | Edit play metadata (title, cast, synopsis) when approving | The entry is accurate before verification |
| US-05 | Any User | See a 'Verified' badge on plays approved by a trusted user | I can trust the accuracy of the information |
| US-06 | Admin | Assign or revoke the Trusted User role from any registered user | I can control who can verify plays |

### 4.2 Reviews & Ratings

| ID | As a... | I want to... | So that... |
|---|---|---|---|
| US-07 | Registered User | Write a text review and assign a star rating (1–5) for a play | I can share my opinion with the community |
| US-08 | Registered User | Edit or delete my own review | I can correct mistakes or change my opinion |
| US-09 | Any User | See an aggregate star rating and total review count on each play page | I can quickly gauge a play's reception |
| US-10 | Any User | Sort reviews by: Most Recent, Top Rated, Most Liked | I can find the most relevant reviews |
| US-11 | Registered User | Review an unverified play | I don't have to wait for verification to share feedback |

### 4.3 Search & Discovery

| ID | As a... | I want to... | So that... |
|---|---|---|---|
| US-12 | Any User | Search plays by title, author, or genre | I can quickly find what I'm looking for |
| US-13 | Any User | Filter plays by verified status, genre, rating, and year | I can narrow results to my preferences |
| US-14 | Any User | See a trending plays feed based on recent review activity | I can discover popular content |
| US-15 | Any User | See a play detail page with metadata, aggregate rating, and all reviews | I can get a full picture of a play |

### 4.4 Social Features

| ID | As a... | I want to... | So that... |
|---|---|---|---|
| US-16 | Registered User | Follow and unfollow other users | I can keep up with reviewers I trust |
| US-17 | Registered User | Like a review | I can show appreciation for quality reviews |
| US-18 | Registered User | Comment on a review (threaded, Reddit-style) | I can engage in discussion about a review |
| US-19 | Registered User | Reply to a comment on a review | I can have threaded conversations |
| US-20 | Registered User | See a feed of recent activity from users I follow | I can stay up to date with my community |
| US-21 | Registered User | Like a comment | I can upvote good discussion contributions |

---

## 5. Functional Requirements

### 5.1 Authentication & Accounts

- Email/password registration with email verification.
- OAuth sign-in via Google and Apple (required for iOS App Store compliance).
- Password reset via email link.
- User profile page: avatar, bio, join date, reviews authored, followers/following count.

### 5.2 Play Entity

Each play record must contain the following fields:

| Field | Required | Notes |
|---|---|---|
| Title | ✅ | — |
| Playwright / Author | ✅ | — |
| Genre | ✅ | Controlled vocabulary: Drama, Comedy, Musical, Tragedy, Experimental, Other |
| Synopsis | ❌ | Max 1,000 characters |
| Original Language | ❌ | — |
| Premiere Date / Year | ❌ | — |
| Production Company | ❌ | — |
| Cover Image | ❌ | Uploaded by submitter or trusted user |
| Verified Status | — | `unverified` \| `verified` \| `rejected` |
| Submitter | — | User reference |
| Verified By | — | Trusted User reference (nullable) |

### 5.3 Play Submission Flow

- Any registered user can submit a new play.
- Before submitting, the system performs a fuzzy title match to warn the user of potential duplicates.
- Submitted plays are immediately visible with an **Unverified** badge.
- All Trusted Users are notified when a new play is pending verification.
- Trusted Users see a **Pending Verification** queue in their dashboard.
- On approval, the play receives the **Verified** badge and the submitter is notified.
- On rejection, the submitter is notified with an optional reason message.

### 5.4 Reviews

- A user may submit **one review per play** and may update it at any time.
- Review fields: star rating 1–5 (required), title (optional, max 120 chars), body (required, min 50 chars, max 5,000 chars).
- Reviews can be written on both verified and unverified plays.
- Aggregate rating = mean of all star ratings, displayed to one decimal place (e.g., 4.2 ★).

### 5.5 Comments (Reddit-style Threading)

- MVP supports **one level of threading**: comment → replies. Deeper nesting is out of scope for v1.
- Comments can be liked by any authenticated user.
- A comment author can delete their own comment. Deleted comments with existing replies display as `[deleted]`.
- Comment and reply counts are shown on each review card.

### 5.6 Social — Follow System

- Open follow model — no approval required.
- A **Following** feed shows recent reviews from followed users, sorted by recency.
- Follower/following counts displayed on user profiles.

### 5.7 Notifications

**In-app notifications triggered by:**
- Play you submitted gets verified or rejected
- Someone likes or comments on your review
- Someone replies to your comment
- Someone starts following you

**Email notifications:**
- Play verified/rejected (always on)
- All other email notifications are opt-in

---

## 6. Non-Functional Requirements

| Category | Requirement |
|---|---|
| **Performance** | Play detail and review list pages load in < 2s on 4G mobile connections |
| **Availability** | 99.5% uptime SLA for MVP |
| **Scalability** | API layer must support horizontal scaling |
| **Security** | JWT-based auth; all endpoints require authentication except browse/search; rate limit review submission to 10/hour per user |
| **Accessibility** | WCAG 2.1 AA compliance for web |
| **Localization** | English only for MVP; architecture must support i18n |
| **Mobile** | iOS 15+ and Android 10+ |

---

## 7. Out of Scope (v1)

- Ticket purchasing or booking integration
- Production/show listings (specific performances with dates, venues, showtimes) — MVP covers the play as a work, not individual productions
- Paid tiers or monetization
- Direct messaging between users
- Comment threading deeper than one level (replies to replies)
- Email digest or activity summary emails
- Mobile push notifications
- Content moderation tooling beyond manual admin actions

---

## 8. Key Data Model (Conceptual)

```
User
  - id, email, password_hash, role (user | trusted | admin)
  - avatar, bio, created_at
  - has many: Reviews, Comments, Likes
  - many-to-many: Follows (follower ↔ followee)

Play
  - id, title, author, genre, synopsis, language, premiere_year
  - production_company, cover_image_url
  - status (unverified | verified | rejected)
  - submitted_by (User FK), verified_by (User FK, nullable)
  - has many: Reviews

Review
  - id, rating (1–5), title, body
  - play_id (Play FK), user_id (User FK)
  - unique constraint: (play_id, user_id)
  - has many: Comments, Likes

Comment
  - id, body, deleted_at
  - review_id (Review FK), user_id (User FK)
  - parent_comment_id (Comment FK, nullable — one level deep only)
  - has many: Likes

Like  [polymorphic]
  - id, user_id (User FK)
  - target_type (review | comment), target_id
  - unique constraint: (user_id, target_type, target_id)

Follow
  - follower_id (User FK), followee_id (User FK)
  - unique constraint: (follower_id, followee_id)
```

---

## 9. Key Acceptance Criteria

### Play Verification Flow

- **Given** a registered user submits a play, the play appears immediately with an **Unverified** badge.
- **Given** a trusted user opens the pending queue, they can see all unverified plays with submitter info.
- **Given** a trusted user approves a play, the **Unverified** badge is replaced by **Verified** within one page reload.
- **Given** a trusted user rejects a play, it is removed from public view and the submitter receives a notification.

### Reviews

- **Given** a registered user submits a review, it appears on the play detail page immediately.
- **Given** two users submit reviews for the same play, the aggregate rating updates correctly.
- **Given** a user attempts to submit a second review on the same play, they are shown their existing review with an edit option — not a new form.

### Comments

- **Given** a user submits a comment on a review, it appears nested below the review body.
- **Given** a user replies to a comment, the reply is threaded under the parent comment.
- **Given** a comment with replies is deleted, the body shows `[deleted]` while replies remain visible.

---

## 10. Open Questions

| # | Question | Decision Needed From |
|---|---|---|
| 1 | How does a user become a Trusted User — admin invitation only, or can users apply? | Product / Operations |
| 2 | Should rejected plays be permanently deleted or archived (invisible but recoverable)? | Product |
| 3 | Should users be able to report reviews or comments for moderation? | Product / Legal |
| 4 | Is there a minimum account age or review count before a user can submit a play? | Product (anti-spam) |
| 5 | Should the Following feed be the default home feed, or a secondary tab? | Design / Product |

---

*StagePass PRD v1.0 — Draft*
