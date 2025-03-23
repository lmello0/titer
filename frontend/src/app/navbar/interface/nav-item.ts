export interface NavItem {
  label: string;
  position?: number;
  rightest?: boolean;

  route?: string | any[];

  useClickHandler?: boolean;
  clickHandler?: (event?: MouseEvent) => void;
}
