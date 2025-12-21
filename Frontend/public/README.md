# Unused Assets

The following directories in `Frontend/public` were identified as unused during a project scan:

- **`blog/`**: Contains blog-related images (e.g., `blog-1.jpg`) that are not referenced in the source code.
- **`features/`**: Contains technology icons (e.g., `react.png`, `tailwindcss.png`) that are not referenced in `data-features.tsx` or elsewhere.
- **`events/`**: Images for the Events section. While potentially referenced in code, the Events page is not accessible via UI navigation (orphaned route).

# Inaccessible Pages & Related Assets

The following pages exist in the codebase (`Frontend/src/app/(routes)`) but are not accessible via the main navigation, footer, or other UI elements (orphaned routes). Consequently, the image assets they reference are effectively unused in the production build.

- **`/history`** (Orphaned Route):
    - Uses images from: **`history/`**
    - Referencing Component: `data-history.tsx`
    - Status: The page exists but is not linked. The `history/` folder images are largely wasted.

- **`/services`** (Orphaned Route):
    - Uses images from: **`events/`** (via `data-events.tsx` import)
    - Referencing Component: `data-events.tsx` is imported here.
    - Status: The page is not linked. Images in `events/` are effectively unused.
    - *Note:* The `/services` page also uses `data-services.tsx` (Service Cards), which **are** used on the Home page. So `services/` folder is partially used.

- **Unused Blocks (Components detected but not implemented in any Page)**:
    - **`InnersPagesBlock`** & **`HomePagesBlock`**: These components are defined but never used.
    - Related Assets: **`sections/`** directory (e.g., `page-about.jpg`, `home-classic.jpg`).
    - Status: Since these blocks are never rendered, the entire `sections/` folder is effectively unused.

- **`/chefs`** (Orphaned Route):
    - Status: Empty or unused route. No specific unique assets identified solely for this route (likely uses `team/` which is shared with Home).

- **`/orders`**:
    - Status: Functional page but not linked in UI. Uses shared assets.
